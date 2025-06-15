package com.trianasalesianos.dam.ArmarioVirtual.dto.tag;

import com.trianasalesianos.dam.ArmarioVirtual.model.Tag;

public record GetTagDto(
        Long id,
        String nombre
) {
    public static GetTagDto from(Tag tag) {
        return new GetTagDto(tag.getId(), tag.getNombre());
    }
}